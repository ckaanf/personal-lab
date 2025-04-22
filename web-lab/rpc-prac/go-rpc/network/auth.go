package network

import (
	"github.com/gin-gonic/gin"
	"go-rpc/types"
	"net/http"
)

func (n *Network) login(c *gin.Context) {
	// Auth Data 생성
	var request types.LoginRequest

	if err := c.ShouldBindJSON(&request); err != nil {
		c.JSON(http.StatusBadRequest, err.Error())
	} else if response, err := n.service.CreateAuth(request.Name); err != nil {
		c.JSON(http.StatusBadRequest, err.Error())
	} else {
		c.JSON(http.StatusOK, response)
	}
}

func (n *Network) verify(c *gin.Context) {
	c.JSON(http.StatusOK, "success")
}
