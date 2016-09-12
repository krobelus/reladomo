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

package com.gs.fw.common.mithra.test;

import com.gs.fw.common.mithra.MithraManagerProvider;
import com.gs.fw.common.mithra.notification.replication.ReplicationNotificationConnectionManager;


public class RemoteMithraReplicationNotificationTestCase extends RemoteMithraNotificationTestCase
{

    public void slaveVmSetUp()
    {
        super.slaveVmSetUp();
        getMithraTestResource().createReplicationNotificationTables(ReplicationNotificationConnectionManager.getInstance());
        MithraManagerProvider.getMithraManager().getReplicationNotificationManager().initializeNotificationPollingThreads();        
    }

    public void slaveVmTearDown()
    {
        MithraManagerProvider.getMithraManager().getReplicationNotificationManager().shutdownReplicationNotification();
        getMithraTestResource().tearDownReplicationNotificationTables(ReplicationNotificationConnectionManager.getInstance());
        MithraManagerProvider.getMithraManager().getReplicationNotificationManager().clearReplicationNotificationMaps();
        super.slaveVmTearDown();
    }
}
