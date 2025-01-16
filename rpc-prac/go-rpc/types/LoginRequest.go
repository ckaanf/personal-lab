package types

type LoginRequest struct {
	Name string `json:"name" binding:"required"`
}
