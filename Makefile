# Does not deploy

all:
	git submodule init
	git submodule update
	(cd flapjax/fx; make)
	(cd flapjax/doc; make)
	(cd website; make)
