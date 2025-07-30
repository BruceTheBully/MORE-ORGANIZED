#!/bin/bash
cd /x/Zalgo
git add .
git commit -m "Auto-sync: $(date +'%Y-%m-%d %H:%M:%S')" || exit 0
git push origin main
