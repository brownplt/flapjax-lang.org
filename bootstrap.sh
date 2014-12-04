set -x

SCALA_VERSION=2.11.2
SBT_VERSION=0.13.5

apt-get update -q

apt-get install -yq \
  curl \
  haskell-platform \
  git \
  docker.io \
  software-properties-common

add-apt-repository ppa:webupd8team/java
apt-get update -q

echo debconf shared/accepted-oracle-license-v1-1 select true | \
  debconf-set-selections

apt-get install -yq oracle-java7-installer

wget -q http://dl.bintray.com/sbt/debian/sbt-${SBT_VERSION}.deb
dpkg -i sbt-${SBT_VERSION}.deb
rm sbt-${SBT_VERSION}.deb

apt-get install -f