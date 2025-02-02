package service

import (
	"go-rpc/config"
	auth "go-rpc/gRPC/proto"
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

func (s *Service) CreateAuth(name string) (*auth.AuthData, error) {
	return s.repository.CreateAuth(name)
}
