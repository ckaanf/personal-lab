package client

import (
	"context"
	"go-rpc/config"
	"go-rpc/gRPC/paseto"
	auth "go-rpc/gRPC/proto"
	"google.golang.org/grpc"
	"google.golang.org/grpc/credentials/insecure"
	"time"
)

type GRPCClient struct {
	client      *grpc.ClientConn
	authClient  auth.AuthServiceClient
	pasetoMaker *paseto.PasetoMaker
}

// NewGRPCClient creates a new gRPC client with authentication capabilities. It establishes a connection to the gRPC server specified in the configuration using insecure credentials. The method initializes the authentication service client and a PASETO token maker.
// 
// Parameters:
//   - cfg: A configuration object containing gRPC server connection details
// 
// Returns:
//   - A pointer to the initialized GRPCClient
//   - An error if the connection or client initialization fails
// 
// The function performs the following steps:
//   1. Creates a new GRPCClient instance
//   2. Establishes a gRPC connection using the provided URL
//   3. Initializes the authentication service client
//   4. Creates a PASETO token maker using the configuration
// 
// Example:
//   client, err := NewGRPCClient(config)
//   if err != nil {
//     // Handle connection error
//   }
func NewGRPCClient(cfg *config.Config) (*GRPCClient, error) {
	c := new(GRPCClient)

	if client, err := grpc.Dial(cfg.GRPC.URL, grpc.WithTransportCredentials(insecure.NewCredentials())); err != nil {
		return nil, err
	} else {
		c.client = client
		c.authClient = auth.NewAuthServiceClient(c.client)
		c.pasetoMaker = paseto.NewPasetoMaker(cfg)
	}
	return c, nil
}

func (g *GRPCClient) CreateAuth(name string) (*auth.AuthData, error) {
	now := time.Now()
	expiredTime := now.Add(30 * time.Minute)

	a := &auth.AuthData{
		Name:       name,
		CreateDate: now.Unix(),
		ExpireDate: expiredTime.Unix(),
	}

	if token, err := g.pasetoMaker.CreateNewToken(*a); err != nil {
		return nil, nil
	} else {
		a.Token = token

		if response, err := g.authClient.CreateAuth(context.Background(), &auth.CreateTokenRequest{Auth: a}); err != nil {
			return nil, err
		} else {
			return response.Auth, nil
		}
	}
}

func (g *GRPCClient) VerifyAuth(token string) (*auth.Verify, error) {
	if response, err := g.authClient.VerifyAuth(context.Background(), &auth.VerifyTokenRequest{Token: token}); err != nil {
		return nil, err
	} else {
		return response.Verify, nil
	}
}
