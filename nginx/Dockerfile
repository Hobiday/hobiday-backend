FROM nginx:latest
# 가상 공간의 nginx 기본 설정파일 삭제 후 작성한 설정파일로 대체dd
#RUN rm /etc/nginx/conf.d/nginx.conf
#RUN rm /etc/nginx/conf.d/default.conf # 추가
#COPY nginx.conf /etc/nginx/conf.d
RUN rm /etc/nginx/default.conf
RUN rm /etc/nginx/nginx.conf
COPY nginx.conf /etc/nginx/nginx.conf