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

	if token, arr := g.pasetoMaker.CreateNewToken(a); arr != nil {
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
