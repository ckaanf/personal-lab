package cmd

import (
	"go-rpc/config"
	"go-rpc/gRPC/client"
	"go-rpc/network"
	"go-rpc/repository"
	"go-rpc/service"
)

type App struct {
	cfg *config.Config

	gRPCClient *client.GRPCClient
	service    *service.Service
	repository *repository.Repository
	network    *network.Network
}

// NewApp creates and initializes a new application instance with the provided configuration.
// It sequentially creates and configures critical components including gRPC client, repository,
// service, and network. If any component fails to initialize, the application panics.
// 
// The initialization process follows a strict order:
// 1. Create gRPC client using the provided configuration
// 2. Create repository using the gRPC client
// 3. Create service using the repository
// 4. Create network using the service and gRPC client
// 
// After successful initialization of all components, the network server is started.
// 
// Parameters:
//   - cfg: Configuration settings for the application initialization
// 
// Panics if any component initialization fails, which will terminate the application.
func NewApp(cfg *config.Config) {
	a := &App{cfg: cfg}

	var err error
	if a.gRPCClient, err = client.NewGRPCClient(cfg); err != nil {
		panic(err)
	} else if a.repository, err = repository.NewRepository(cfg, a.gRPCClient); err != nil {
		panic(err)
	} else if a.service, err = service.NewService(cfg, a.repository); err != nil {
		panic(err)
	} else if a.network, err = network.NewNetwork(cfg, a.service, a.gRPCClient); err != nil {
		panic(err)
	} else {
		a.network.StartServer()
	}
}
