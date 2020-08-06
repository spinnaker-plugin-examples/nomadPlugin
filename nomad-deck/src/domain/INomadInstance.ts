import { IInstance } from '@spinnaker/core';

export interface INomadInstance extends IInstance {
  details?: string;
}
