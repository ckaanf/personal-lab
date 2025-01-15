package main

import (
	"flag"
	"go-rpc/cmd"
	"go-rpc/config"
)

var configFlag = flag.String("config", "./config.toml", "config path")

func main() {
	flag.Parse()
	cfg := config.NewConfig(*configFlag)
	cmd.NewApp(cfg)
}
