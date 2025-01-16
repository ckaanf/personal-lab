package paseto

import (
	"github.com/o1egl/paseto"
	"go-rpc/config"
)

type PasetoMaker struct {
	Pt  *paseto.V2
	Key []byte
}

func NewPasetoMaker(cfg config.Config) *PasetoMaker {
	return &PasetoMaker{
		Pt:  paseto.NewV2(),
		Key: []byte(cfg.Paseto.Key),
	}
}

func (m *PasetoMaker) CreateNewToken() (string, error) {
	return "", nil
}

func (m *PasetoMaker) Verify(token string) error {
	return nil
}
