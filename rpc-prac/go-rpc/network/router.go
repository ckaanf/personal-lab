package network

import (
	"github.com/gin-gonic/gin"
	"go-rpc/config"
	"go-rpc/gRPC/client"
	"go-rpc/service"
)

type Network struct {
	cfg        *config.Config
	service    *service.Service
	gRPCClient *client.GRPCClient
	engin      *gin.Engine
}

//   - An error if initialization fails
func NewNetwork(cfg *config.Config, service *service.Service, gRPCClient *client.GRPCClient) (*Network, error) {
	r := &Network{cfg: cfg, service: service, engin: gin.New(), gRPCClient: gRPCClient}

	// 1. token 생성
	r.engin.POST("/login", r.login)
	// 2. token 검증
	r.engin.GET("/verify", r.verifyLogin(), r.verify)
	return r, nil
}

func (n *Network) StartServer() {
	n.engin.Run(":8080")
}
