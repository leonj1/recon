#!/bin/bash

CURRENT_BRANCH=`git branch | grep \* | cut -d ' ' -f2`

BRANCH=$(git rev-parse --abbrev-ref HEAD)
if [[ "$BRANCH" != "master" ]]; then
  echo "Not master branch. Terminating"
  exit 0;
fi

if [ $# -eq 0 ]
  then
    # get the highest tag number
    VERSION=`git describe --abbrev=0 --tags`
    # replace . with space so can split into array
    VERSION_BITS=(${VERSION//./ })
    # get number of parts and increase the last one by 1
    VNUM1=#{VERSION_BITS[0]}
    VNUM2=#{VERSION_BITS[1]}
    VNUM3=#{VERSION_BITS[2]}
    VNUM3=$((VNUM3+1))
    # create new tag
    NEW_TAG="$VNUM1.$VNUM2.$VNUM3"
    echo "Proposing $VERSION to $NEW_TAG"
else
   NEW_TAG=$1
fi

# get current hash and see if it already has a tag
GIT_COMMIT=`git rev-parse HEAD`
NEEDS_TAG=`git describe --contains $GIT_COMMIT`

# only tag if no tag already exists (would be better if the git describe command above could have a silent option)
if [ -z "$NEEDS_TAG" ]; then
   echo "Tagged with $NEW_TAG (Ignoring fatal:cannot describe - this eans commit is untagged)"
   git tag $NEW_TAG
   git push --tags
else
   echo "Already a tag on this commit"
fi

