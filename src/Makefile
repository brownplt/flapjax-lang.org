# Does not deploy

all:
	git submodule init
	git submodule update
	(cd flapjax/fx; make)
	(cd flapjax/doc; make)
	(cd website; make)

docker:
	sudo docker.io build -t docker-registry.c.arjun-umass.internal:5000/flapjax-website .

push:
	sudo docker.io push docker-registry.c.arjun-umass.internal:5000/flapjax-website

testserver:
	docker run -p 0.0.0.0:8080:80 --hostname="www.flapjax-lang.org" --name="flapjax" -t localhost:5000/flapjax-website

provision:
	gcloud compute --project=arjun-umass instances create flapjax-website \
    --image container-vm-v20140826 \
    --image-project google-containers \
    --metadata-from-file google-container-manifest=containers.yaml \
    --zone us-central1-a \
    --address 23.236.54.173 \
    --tags http-server \
    --machine-type f1-micro