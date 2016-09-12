#!/bin/ksh

export CUR_DIR=`pwd`
export RELADOMO_HOME=${RELADOMO_HOME:-"$CUR_DIR/.."}
# todo: release: remove path
export JDK_HOME=${RELADOMO_JDK_HOME:-"/jdk1.6/default/dir"}

# no need to modify stuff below:

export GENERATE_RELADOMO_CONCRETE_CLASSES=true

#  Copyright 2016 Goldman Sachs.
#  Licensed under the Apache License, Version 2.0 (the "License");
#  you may not use this file except in compliance with the License.
#  You may obtain a copy of the License at
#
#    http://www.apache.org/licenses/LICENSE-2.0
#
#  Unless required by applicable law or agreed to in writing,
#  software distributed under the License is distributed on an
#  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
#  KIND, either express or implied.  See the License for the
#  specific language governing permissions and limitations
#  under the License.
