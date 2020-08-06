import { IServerGroupDetailsSectionProps } from '@spinnaker/core';

import {INomadServerGroup} from "../../../domain";

export interface INomadServerGroupDetailsSectionProps extends IServerGroupDetailsSectionProps {
  serverGroup: INomadServerGroup;
}
