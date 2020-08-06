import React from 'react';

import { AccountTag, CollapsibleSection, timestamp } from '@spinnaker/core';
import {INomadServerGroupDetailsSectionProps} from "./INomadServerGroupDetailsSectionProps";

export class ServerGroupTasksSection extends React.Component<INomadServerGroupDetailsSectionProps> {
  constructor(props: INomadServerGroupDetailsSectionProps) {
    super(props);
  }

  public render(): JSX.Element {
    const { serverGroup } = this.props;
    return (
      <CollapsibleSection heading="Task" defaultExpanded={true}>
          <div>
            {serverGroup.taskGroup.Tasks.map((task, index) => (
                <dl key={index} className="dl-horizontal dl-flex">
                  <dt>Task Name</dt>
                  <dd>{task.Name}</dd>
                  <dt>Driver</dt>
                  <dd>{task.Driver}</dd>
                  <dt>Image</dt>
                  <dd>{task.Config.image}</dd>
                  <dt>CPU</dt>
                  <dd>{task.Resources.CPU}</dd>
                  <dt>Disk</dt>
                  <dd>{task.Resources.DiskMB}mb</dd>
                  <dt>Memory</dt>
                  <dd>{task.Resources.MemoryMB}mb</dd>
                  <dt>IOPS</dt>
                  <dd>{task.Resources.IOPS}</dd>
                </dl>
            ))}
          </div>
      </CollapsibleSection>
    );
  }
}
