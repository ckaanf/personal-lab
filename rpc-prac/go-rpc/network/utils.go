package network

import (
	"github.com/gin-gonic/gin"
	"net/http"
	"strings"
)

func (n *Network) verifyLogin() gin.HandlerFunc {
	return func(c *gin.Context) {
		// Verify login
		token := getAuthToken(c)

		if token == "" {
			c.JSON(http.StatusUnauthorized, nil)
			c.Abort()
		} else {
			if _, err := n.gRPCClient.VerifyAuth(token); err != nil {
				c.JSON(http.StatusUnauthorized, nil)
				c.Abort()
			} else {
				c.Next()
			}
		}
	}
}

func getAuthToken(c *gin.Context) string {
	var token string

	authToken := c.Request.Header.Get("Authorization")
	// Bearer token
	authArr := strings.Split(authToken, " ")
	if len(authArr) > 1 {
		token = authArr[1]
	}

	return token
}
