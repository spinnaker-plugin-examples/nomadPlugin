import React from 'react';

import { AccountTag, CollapsibleSection, timestamp } from '@spinnaker/core';
import {INomadServerGroupDetailsSectionProps} from "./INomadServerGroupDetailsSectionProps";

export class ServerGroupTaskGroupSection extends React.Component<INomadServerGroupDetailsSectionProps> {
  constructor(props: INomadServerGroupDetailsSectionProps) {
    super(props);
  }

  public render(): JSX.Element {
    const { serverGroup } = this.props;
    return (
      <CollapsibleSection heading="Task Group" defaultExpanded={true}>
        <dl className="dl-horizontal dl-flex">
          <dt>Task Group Name</dt>
          <dd>{serverGroup.taskGroup.Name}</dd>
          <dt>Size</dt>
          <dd>{serverGroup.taskGroup.EphemeralDisk.SizeMB}mb</dd>
          <dt>Canary</dt>
          <dd>{serverGroup.taskGroup.Update.Canary}</dd>
          <dt>Auto Promote</dt>
          <dd>{serverGroup.taskGroup.Update.AutoPromote.toString()}</dd>
          <dt>Auto Revert</dt>
          <dd>{serverGroup.taskGroup.Update.AutoRevert.toString()}</dd>
        </dl>

      </CollapsibleSection>
    );
  }
}
