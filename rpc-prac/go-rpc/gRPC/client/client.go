package client

import (
	"go-rpc/config"
	"go-rpc/gRPC/paseto"
	auth "go-rpc/gRPC/proto"
	"google.golang.org/grpc"
	"google.golang.org/grpc/credentials/insecure"
)

type GRPCClient struct {
	client      *grpc.ClientConn
	authClient  auth.AuthServiceClient
	pasetoMaker *paseto.PasetoMaker
}

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

func (g *GRPCClient) CreateAuth(address string) (*auth.AuthData, error) {
	return nil, nil
}

func (g *GRPCClient) VerifyAuth(token string) (*auth.VerifyTokenResponse, error) {
	return nil, nil
}
