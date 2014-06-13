# Does not deploy

all:
	git submodule init
	git submodule update
	(cd flapjax/fx; make)
	(cd flapjax/doc; make)
	(cd website; make)

docker:
	docker build -t localhost:5000/flapjax-website .

push:
	docker push localhost:5000/flapjax-website

testserver:
	docker run -p 0.0.0.0:8080:80 --hostname="www.flapjax-lang.org" --name="flapjax" -t localhost:5000/flapjax-website
