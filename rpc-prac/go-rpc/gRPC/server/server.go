package server

import (
	"context"
	"errors"
	"go-rpc/config"
	"go-rpc/gRPC/paseto"
	auth "go-rpc/gRPC/proto"
	"google.golang.org/grpc"
	"google.golang.org/grpc/reflection"
	"log"
	"net"
	"time"
)

type GRPCServer struct {
	auth.AuthServiceServer
	pasetoMaker *paseto.PasetoMaker
	// DB 연결 안해서 사용
	tokenVerifyMap map[string]*auth.AuthData
}

// NewGRPCServer creates and starts a new gRPC server for authentication services. It configures the server to listen on a specified TCP address, registers the authentication service, and starts serving requests asynchronously.
// 
// The function takes a configuration object and sets up a gRPC server with the following features:
// - Listens on a TCP network address specified in the configuration
// - Registers an AuthServiceServer with a new PASETO token maker
// - Initializes an empty token verification map
// - Enables gRPC reflection for service discovery
// - Starts the server in a separate goroutine
//
// Parameters:
//   - cfg: A configuration object containing gRPC server settings
//
// Returns:
//   - An error if the server cannot be initialized or started, otherwise nil
//
// Note: The server is started asynchronously and will panic if it fails to serve requests
func NewGRPCServer(cfg *config.Config) error {
	if listen, err := net.Listen("tcp", cfg.GRPC.URL); err != nil {
		return err
	} else {

		// AuthServiceServe
		// ResgisterAuthServiceServer
		server := grpc.NewServer([]grpc.ServerOption{}...)
		auth.RegisterAuthServiceServer(server, &GRPCServer{
			pasetoMaker:    paseto.NewPasetoMaker(cfg),
			tokenVerifyMap: make(map[string]*auth.AuthData),
		})

		reflection.Register(server)
		go func() {
			log.Println("Start GRPC Server")
			if err = server.Serve(listen); err != nil {
				panic(err)
			}
		}()
	}
	return nil
}

func (s *GRPCServer) CreateAuth(_ context.Context, request *auth.CreateTokenRequest) (*auth.CreateTokenResponse, error) {
	data := request.Auth
	token := data.Token

	s.tokenVerifyMap[token] = data

	return &auth.CreateTokenResponse{Auth: data}, nil
}

func (s *GRPCServer) VerifyAuth(_ context.Context, request *auth.VerifyTokenRequest) (*auth.VerifyTokenResponse, error) {
	token := request.Token

	response := &auth.VerifyTokenResponse{Verify: &auth.Verify{Auth: nil}}

	if authData, ok := s.tokenVerifyMap[token]; !ok {
		response.Verify.Status = auth.ResponseType_FAILED
		return response, errors.New("Not Existed At Map")
	} else if err := s.pasetoMaker.VerifyToken(token); err != nil {
		log.Println("error", err.Error())
		return nil, errors.New("Failed Verify token")
	} else if authData.ExpireDate < time.Now().Unix() {
		delete(s.tokenVerifyMap, token)
		response.Verify.Status = auth.ResponseType_EXPIRED_DATE
		return response, errors.New("Expired Time Over")
	} else {
		response.Verify.Status = auth.ResponseType_SUCCESS
	}
	return response, nil
}

func (s *GRPCServer) mustEmbedUnimplementedAuthServiceServer() {
	panic("implement me")
}
