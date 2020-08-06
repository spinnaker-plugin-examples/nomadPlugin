import React from 'react';

import { AccountTag, CollapsibleSection, timestamp } from '@spinnaker/core';
import {INomadServerGroupDetailsSectionProps} from "./INomadServerGroupDetailsSectionProps";

export class ServerGroupJobSection extends React.Component<INomadServerGroupDetailsSectionProps> {
  constructor(props: INomadServerGroupDetailsSectionProps) {
    super(props);
  }

  public render(): JSX.Element {
    const { serverGroup } = this.props;
    return (
      <CollapsibleSection heading="Job" defaultExpanded={true}>
        <dl className="dl-horizontal dl-flex">
          <dt>Namespace</dt>
          <dd>{serverGroup.job.Namespace}</dd>
          <dt>Name</dt>
          <dd>{serverGroup.job.Name}</dd>
          <dt>Version</dt>
          <dd>{serverGroup.job.Version}</dd>
          <dt>Type</dt>
          <dd>{serverGroup.job.Type}</dd>
          <dt>Status</dt>
          <dd>{serverGroup.job.Status}</dd>
          <dt>Region</dt>
          <dd>{serverGroup.job.Region}</dd>
          <dt>Datacenters</dt>
          <dd>{serverGroup.job.Datacenters}</dd>
        </dl>
      </CollapsibleSection>
    );
  }
}
