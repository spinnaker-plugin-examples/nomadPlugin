import React from 'react';

import {
  ClusterTargetBuilder,
  IOwnerOption,
  IServerGroupActionsProps,
  IServerGroupJob,
  NgReact,
  SETTINGS,
} from '@spinnaker/core';
import {INomadServerGroup} from "../../domain";


export interface INomadServerGroupActionsProps extends IServerGroupActionsProps {
  serverGroup: INomadServerGroup;
}

export interface INomadServerGroupJob extends IServerGroupJob {
  serverGroupId: string;
}

export class NomadServerGroupActions extends React.Component<INomadServerGroupActionsProps> {

  public render(): JSX.Element {
    const { app, serverGroup } = this.props;
    const { AddEntityTagLinks } = NgReact;
    const showEntityTags = SETTINGS.feature && SETTINGS.feature.entityTags;
    const entityTagTargets: IOwnerOption[] = ClusterTargetBuilder.buildClusterTargets(serverGroup);

    return (
        <div>
          {showEntityTags && (
              <AddEntityTagLinks
                  component={serverGroup}
                  application={app}
                  entityType="serverGroup"
                  ownerOptions={entityTagTargets}
                  onUpdate={() => app.serverGroups.refresh()}
              />
          )}
        </div>
    );
  }
}
