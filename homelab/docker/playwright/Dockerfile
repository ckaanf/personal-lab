# 화면 확인용 브라우저. 공식 이미지에는 브라우저만 있고 파이썬 패키지는 없어서 같은 버전으로 넣는다.
# 상시 실행하지 않는다: docker run --rm 으로 필요할 때만 띄운다.
FROM mcr.microsoft.com/playwright/python:v1.63.0-noble
RUN pip install --no-cache-dir --break-system-packages playwright==1.63.0
