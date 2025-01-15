package repository

import "go-rpc/config"

type Repository struct {
	cfg *config.Config
}

func NewRepository(cfg *config.Config) (*Repository, error) {
	r := &Repository{cfg: cfg}

	return r, nil
}
