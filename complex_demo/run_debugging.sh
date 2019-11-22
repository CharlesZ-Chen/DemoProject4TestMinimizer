#!/bin/bash

expect_rtn_code=1

expect_regex="AsSuperVisitor: type is not an erased subtype of supertype."

debugged_tool="check"

checker="org.checkerframework.checker.nullness.NullnessChecker"

if [ ! -d "../do-like-javac" ] ; then
    (cd ../ && git clone https://github.com/opprop/do-like-javac.git)
fi

DLJC="../do-like-javac"

#parsing build command of the target program
build_cmd=ant

debug_cmd="python $DLJC/dljc -t testminimizer --debuggedTool $debugged_tool --expectReturnCode $expect_rtn_code  --checker $checker --expectOutputRegex \"$expect_regex\" -- $build_cmd "

echo "============ Important variables ============="
echo "build cmd: $build_cmd"
echo "running cmd: $debug_cmd"
echo "============================================="

eval "$debug_cmd"

echo "---- run_debugging.sh done. ----"
