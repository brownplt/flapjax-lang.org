all: jar

jar:
	sbt compile assembly

container:
	sudo docker.io build -t  gcr.io/arjun_umass/flapjax_compiler .

push:
	gcloud preview docker push gcr.io/arjun_umass/flapjax_compiler
