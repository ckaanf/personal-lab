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
