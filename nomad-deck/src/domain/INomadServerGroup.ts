import { IServerGroup } from '@spinnaker/core';

import { INomadInstance } from './INomadInstance';

export interface INomadServerGroup extends IServerGroup {
  instances: INomadInstance[];
  job: INomadJob
  taskGroup: INomadTaskGroup
}

export interface INomadJob {
  Name: string;
  Region: string;
  Namespace: string;
  Version: number;
  Status: string;
  Type: string;
  Datacenters: string[];
}

export interface INomadTaskGroup {
  Name: string;
  EphemeralDisk: INomadTaskGroupEphemeralDisk;
  Update: INomadTaskGroupUpdate;
  Tasks: INomadTask[];
}

export interface INomadTaskGroupEphemeralDisk {
  SizeMB: number;
}

export interface INomadTaskGroupUpdate {
  Canary: number;
  AutoPromote: boolean;
  AutoRevert: boolean;
}

export interface INomadTask {
  Name: string;
  Driver: string;
  Config: INomadTaskConfig;
  Resources: INomadTaskResources;
}

export interface INomadTaskConfig {
  image: string;
}

export interface INomadTaskResources {
  CPU: number;
  DiskMB: number;
  MemoryMB: number;
  IOPS: number;
}
