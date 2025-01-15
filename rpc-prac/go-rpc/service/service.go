package service

import (
	"go-rpc/config"
	"go-rpc/repository"
)

type Service struct {
	cfg *config.Config

	repository *repository.Repository
}

func NewService(cfg *config.Config, repository *repository.Repository) (*Service, error) {
	r := &Service{cfg: cfg, repository: repository}

	return r, nil
}
