package main

import (
	"fmt"
	"io"
	"io/ioutil"
	"log"
	"net/http"
	"os/exec"
	"time"
)

func convertWebMToMp4(inputPath string, outputPath string) error {
	cmd := exec.Command("ffmpeg", "-i", inputPath, "-vcodec", "libx264", "-acodec", "aac", "-strict", "experimental", outputPath)
	err := cmd.Run()

	if err != nil {
		fmt.Println("Error converting file:", err)
		return err
	}
	return nil
}

func serveStaticFile(w http.ResponseWriter, r *http.Request) {
	http.ServeFile(w, r, "./static/index.html")
}

func uploadHandler(w http.ResponseWriter, r *http.Request) {
	file, _, err := r.FormFile("video")

	if err != nil {
		http.Error(w, "Error formfile", http.StatusBadRequest)
		return
	}

	defer file.Close()

	now := time.Now().Format("2006-01-02_15-04-05")
	filder := "recorded"

	tempFile, err := ioutil.TempFile(filder, fmt.Sprintf("%s_*.webm", now))
	defer tempFile.Close()

	_, err = io.Copy(tempFile, file)

	if err != nil {
		fmt.Println("Error copying file", err)
		return
	}

	outputFile := fmt.Sprintf("%s/%s.mp4", filder, now)
	err = convertWebMToMp4(tempFile.Name(), outputFile)

	if err != nil {
		return
	}

	http.ServeFile(w, r, outputFile)

}

func main() {
	http.HandleFunc("/", serveStaticFile)
	http.HandleFunc("/upload", uploadHandler)
	log.Println("Uploading")
	http.ListenAndServe(":8080", nil)
}
