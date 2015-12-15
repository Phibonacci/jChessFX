#!/bin/sh
git log --pretty=format:"%ad %ae %s" --date=iso | sed -e 's/jean.fauquenot@gmail.com/Jean       /g' | sed -e s/leduc_p@epitech.eu/Paul-Maxime/g
echo
