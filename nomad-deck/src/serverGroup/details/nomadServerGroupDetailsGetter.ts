import { IPromise } from 'angular';

import { isEmpty } from 'lodash';
import { Observable } from 'rxjs';

import { IServerGroupDetailsProps, ServerGroupReader } from '@spinnaker/core';
import {INomadServerGroup} from "../../domain";

function extractServerGroupSummary(props: IServerGroupDetailsProps): IPromise<INomadServerGroup> {
  const { app, serverGroup } = props;
  return app.ready().then(() => {
    let summary: INomadServerGroup = app.serverGroups.data.find((toCheck: INomadServerGroup) => {
      return (
        toCheck.name === serverGroup.name &&
        toCheck.account === serverGroup.accountId &&
        toCheck.region === serverGroup.region
      );
    });
    return summary;
  });
}

export function nomadServerGroupDetailsGetter(
  props: IServerGroupDetailsProps,
  autoClose: () => void,
): Observable<INomadServerGroup> {
  const { app, serverGroup: serverGroupInfo } = props;
  return new Observable<INomadServerGroup>(observer => {
    extractServerGroupSummary(props).then(summary => {
      ServerGroupReader.getServerGroup(
        app.name,
        serverGroupInfo.accountId,
        serverGroupInfo.region,
        serverGroupInfo.name,
      ).then((serverGroup: INomadServerGroup) => {
        // it's possible the summary was not found because the clusters are still loading
        Object.assign(serverGroup, summary, { account: serverGroupInfo.accountId });

        if (!isEmpty(serverGroup)) {
          observer.next(serverGroup);
        } else {
          autoClose();
        }
      }, autoClose);
    }, autoClose);
  });
}
