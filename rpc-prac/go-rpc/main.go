package main

import (
	"flag"
	"go-rpc/cmd"
	"go-rpc/config"
	"go-rpc/gRPC/server"
	"time"
)

var configFlag = flag.String("config", "./config.toml", "config path")

// main is the entry point of the gRPC server application. It parses command-line flags to load a configuration file,
// initializes a gRPC server with the loaded configuration, and then creates a new application instance.
// If server initialization fails, the program panics, terminating execution.
func main() {
	flag.Parse()
	cfg := config.NewConfig(*configFlag)

	if err := server.NewGRPCServer(cfg); err != nil {
		panic(err)
	} else {
		time.Sleep(1e9)
		cmd.NewApp(cfg)
	}
}
