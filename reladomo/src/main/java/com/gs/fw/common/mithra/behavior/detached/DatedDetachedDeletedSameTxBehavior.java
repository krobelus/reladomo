/*
 Copyright 2016 Goldman Sachs.
 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing,
 software distributed under the License is distributed on an
 "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 KIND, either express or implied.  See the License for the
 specific language governing permissions and limitations
 under the License.
 */

package com.gs.fw.common.mithra.behavior.detached;

import com.gs.fw.common.mithra.MithraDataObject;
import com.gs.fw.common.mithra.MithraDatedTransactionalObject;
import com.gs.fw.common.mithra.MithraTransactionalObject;
import com.gs.fw.common.mithra.behavior.state.DatedPersistenceState;
import com.gs.fw.common.mithra.finder.RelatedFinder;
import com.gs.fw.common.mithra.util.StatisticCounter;
import java.util.Map;


public class DatedDetachedDeletedSameTxBehavior extends DatedDetachedDeletedBehavior
{

    public void resetDetachedData(MithraDatedTransactionalObject mithraObject, MithraDataObject newData)
    {
        mithraObject.zSetTxData(newData);
        mithraObject.zSetTxPersistenceState(DatedPersistenceState.DETACHED);
    }

    @Override
    public Map<RelatedFinder, StatisticCounter> addNavigatedRelationshipsStats(MithraTransactionalObject obj, RelatedFinder parentFinder, Map<RelatedFinder, StatisticCounter> navigationStats)
    {
        return obj.zAddNavigatedRelationshipsStatsForDelete(parentFinder, navigationStats);
    }
}
