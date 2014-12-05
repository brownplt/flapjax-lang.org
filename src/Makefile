# Does not deploy

all:
	git submodule init
	git submodule update
	(cd flapjax/fx; make)
	(cd flapjax/doc; make)
	(cd website; make)

testserver:
	docker run -d -p 0.0.0.0:8080:80 --net="host" --name="website" -t docker-registry.c.arjun-umass.internal:5000/flapjax-website
	docker run -d --net="host" --name="compiler" -t docker-registry.c.arjun-umass.internal:5000/flapjax-compiler

killserver:
	docker rm -f compiler || true
	docker rm -f website || true

push:
	(cd website && make push)
	(cd server && make push)

provision:
	gcloud compute --project=arjun-umass instances create flapjax-website \
    --image container-vm-v20140929 \
    --image-project google-containers \
    --metadata-from-file google-container-manifest=containers.yaml \
    --zone us-central1-a \
    --address 23.236.54.173 \
    --tags http-server \
    --machine-type f1-micro

unprovision:
	gcloud compute instances delete --zone=us-central1-a flapjax-website