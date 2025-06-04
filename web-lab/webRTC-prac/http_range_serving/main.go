package main

import (
	"fmt"
	"io"
	"net/http"
	"os"
	"path/filepath"
	"strconv"
	"strings"
)

const uploadDir = "./uploads"

func main() {
	err := os.MkdirAll(uploadDir, os.ModePerm)
	if err != nil {
		panic(err)
	}

	http.HandleFunc("/", serveHTML)
	http.HandleFunc("/upload", handleUpload)
	http.HandleFunc("/video", handleVideo)

	fmt.Println("server started")
	http.ListenAndServe(":8080", nil)
}

func serveHTML(w http.ResponseWriter, r *http.Request) {
	http.ServeFile(w, r, "./static/index.html")
}

func handleUpload(w http.ResponseWriter, r *http.Request) {
	file, handler, err := r.FormFile("file")
	if err != nil {
		http.Error(w, "Failed to create file", http.StatusBadRequest)
		return
	}

	defer file.Close()

	fileName := r.FormValue("fileName")

	if fileName == "" {
		fileName = handler.Filename
	}

	destPath := filepath.Join(uploadDir, fileName)
	destFile, err := os.Create(destPath)
	if err != nil {
		http.Error(w, "Failed to create destination", http.StatusInternalServerError)
		return
	}

	defer destFile.Close()

	buffer := make([]byte, 1024*1024) // 1MB

	for {
		n, err := file.Read(buffer)
		if err != nil && err != io.EOF {
			http.Error(w, "Failed to read destination", http.StatusInternalServerError)
			return
		}

		if n == 0 {
			break
		}

		_, err = destFile.Write(buffer[:n])

		if err != nil {
			http.Error(w, "Failed to write destination", http.StatusInternalServerError)
			return
		}
	}

	fmt.Println("Success to create destination")
}

func handleVideo(w http.ResponseWriter, r *http.Request) {
	fileName := r.URL.Query().Get("fileName")
	if fileName == "" {
		http.Error(w, "Invalid file name", http.StatusBadRequest)
		return
	}

	filepath := filepath.Join(uploadDir, fileName)
	file, err := os.Open(filepath)
	if err != nil {
		http.Error(w, "Invalid file", http.StatusInternalServerError)
		return
	}

	defer file.Close()

	rangeHeader := r.Header.Get("Range")
	if rangeHeader == "" {
		http.ServeFile(w, r, filepath)
		return
	}

	// Range : byte=313072-621321
	// bytes=<start>-<end>
	// 313072번째 바이트(offset)에서 621321까지의 모든 데이터를 요청

	rangePars := strings.Split(strings.TrimPrefix(rangeHeader, "bytes="), "-")
	start, _ := strconv.ParseInt(rangePars[0], 10, 64)

	stat, _ := file.Stat()
	fileSize := stat.Size()

	var end int64

	if len(rangePars) > 1 && rangePars[1] != "" {
		end, _ = strconv.ParseInt(rangePars[1], 10, 64)
	} else {
		end = fileSize - 1
	}

	if end >= fileSize {
		end = fileSize - 1
	}

	w.Header().Set("Content-Type", "video/mp4")
	w.Header().Set("Content-Range", fmt.Sprintf("bytes %d-%d/%d", start, end, fileSize))
	w.Header().Set("Accept-Ranges", "bytes")
	// 중간 요청 시 값 필요
	w.WriteHeader(http.StatusPartialContent)

	file.Seek(start, io.SeekStart)
	io.CopyN(w, file, end-start+1)
}
