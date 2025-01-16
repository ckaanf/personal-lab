package main

import (
	"flag"
	"go-rpc/cmd"
	"go-rpc/config"
	"go-rpc/gRPC/server"
	"time"
)

var configFlag = flag.String("config", "./config.toml", "config path")

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
