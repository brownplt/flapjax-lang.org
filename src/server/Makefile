all: container

jar:
	sbt compile assembly

container:
	sudo docker.io build -t docker-registry.c.arjun-umass.internal:5000/flapjax-compiler .

push:
	sudo docker.io push docker-registry.c.arjun-umass.internal:5000/flapjax-compiler
