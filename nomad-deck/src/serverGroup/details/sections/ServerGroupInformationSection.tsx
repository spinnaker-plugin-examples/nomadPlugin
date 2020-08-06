import React from 'react';

import { AccountTag, CollapsibleSection, timestamp } from '@spinnaker/core';
import {INomadServerGroupDetailsSectionProps} from "./INomadServerGroupDetailsSectionProps";

export class ServerGroupInformationSection extends React.Component<INomadServerGroupDetailsSectionProps> {
  constructor(props: INomadServerGroupDetailsSectionProps) {
    super(props);
  }

  public render(): JSX.Element {
    const { serverGroup } = this.props;
    return (
      <CollapsibleSection heading="Server Group Information" defaultExpanded={true}>
        <dl className="dl-horizontal dl-flex">
          <dt>Created</dt>
          <dd>{timestamp(serverGroup.createdTime.toString().slice(0, 13))}</dd>
          <dt>Provider</dt>
          <dd>{serverGroup.cloudProvider}</dd>
          <dt>Account</dt>
          <dd>
            <AccountTag account={serverGroup.account} />
          </dd>
          <dt>Application</dt>
          <dd>{serverGroup.app}</dd>
          <dt>Cluster</dt>
          <dd>{serverGroup.cluster}</dd>
          <dt>Detail</dt>
          <dd>{serverGroup.detail}</dd>
          <dt>Stack</dt>
          <dd>{serverGroup.stack}</dd>
          <dt>Desired Capacity</dt>
          <dd>{serverGroup.capacity.desired}</dd>
        </dl>
      </CollapsibleSection>
    );
  }
}
