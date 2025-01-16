package config

import (
	"github.com/naoina/toml"
	"os"
)

type Config struct {
	Paseto struct {
		Key string
	}

	GRPC struct {
		URL string
	}
}

func NewConfig(path string) *Config {
	c := new(Config)

	if file, err := os.Open(path); err != nil {
		panic(err)
	} else {
		defer file.Close()
		if err = toml.NewDecoder(file).Decode(c); err != nil {
			panic(err)
		} else {
			return c
		}
	}
}
