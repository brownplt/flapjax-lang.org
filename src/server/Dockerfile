# TODO: must be 32-bit!!
# Is the current flapjax server dead? Java is not running

FROM ubuntu:precise

MAINTAINER Arjun Guha <arjun@cs.umass.edu>

RUN apt-get update
RUN apt-get upgrade -y
RUN apt-get install -y libgmp3c2
RUN apt-get install -y python-software-properties

# https://gist.github.com/henrik-muehe/6155333
RUN apt-get install libfuse2
RUN cd /tmp ; apt-get download fuse
RUN cd /tmp ; dpkg-deb -x fuse_* .
RUN cd /tmp ; dpkg-deb -e fuse_*
RUN cd /tmp ; rm fuse_*.deb
RUN cd /tmp ; echo -en '#!/bin/bash\nexit 0\n' > DEBIAN/postinst
RUN cd /tmp ; dpkg-deb -b . /fuse.deb
RUN cd /tmp ; dpkg -i /fuse.deb

RUN apt-get install -y ia32-libs
RUN add-apt-repository -y ppa:webupd8team/java
RUN apt-get update
RUN echo oracle-java8-installer shared/accepted-oracle-license-v1-1 select true | /usr/bin/debconf-set-selections
RUN apt-get install -y oracle-java8-installer

ADD fxc_2.0_i386.deb /root/fxc_2.0.i386.deb
RUN echo "foreign-architecture i386" > /etc/dpkg/dpkg.cfg.d/architectures
RUN apt-get install -y libgmp3c2:i386
RUN dpkg  --force-depends -i /root/fxc_2.0.i386.deb

ADD target/scala-2.10/flapjax-server-assembly-2.0.jar \
  /root/flapjax-server-assembly-2.0.jar

EXPOSE 8081


CMD ["/usr/bin/java", "-jar", "/root/flapjax-server-assembly-2.0.jar", "serve"]
