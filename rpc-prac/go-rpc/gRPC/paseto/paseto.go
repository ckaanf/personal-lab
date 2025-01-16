package paseto

import (
	"github.com/o1egl/paseto"
	"go-rpc/config"
	auth "go-rpc/gRPC/proto"
)

type PasetoMaker struct {
	Pt  *paseto.V2
	Key []byte
}

// The function returns a pointer to the newly created PasetoMaker.
func NewPasetoMaker(cfg *config.Config) *PasetoMaker {
	return &PasetoMaker{
		Pt:  paseto.NewV2(),
		Key: []byte(cfg.Paseto.Key),
	}
}

// Go Lang 에서 마샬링을 할 때 보통 포인터 타입을 넘기지 않는다
func (m *PasetoMaker) CreateNewToken(auth auth.AuthData) (string, error) {
	return m.Pt.Encrypt(m.Key, auth, nil)
}

// Decoding을 할 때에는 포인터 타입을 직접적으로 넘겨줌으로써 byte를 받음
func (m *PasetoMaker) VerifyToken(token string) error {
	var auth auth.AuthData
	return m.Pt.Decrypt(token, m.Key, &auth, nil)
}
