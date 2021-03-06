///
/// softvis3d-frontend
/// Copyright (C) 2016 Stefan Rinderle and Yvo Niedrich
/// stefan@rinderle.info / yvo.niedrich@gmail.com
///
/// This program is free software; you can redistribute it and/or
/// modify it under the terms of the GNU Lesser General Public
/// License as published by the Free Software Foundation; either
/// version 3 of the License, or (at your option) any later version.
///
/// This program is distributed in the hope that it will be useful,
/// but WITHOUT ANY WARRANTY; without even the implied warranty of
/// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
/// Lesser General Public License for more details.
///
/// You should have received a copy of the GNU Lesser General Public
/// License along with this program; if not, write to the Free Software
/// Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
///

import ReactElement = __React.ReactElement;
declare const config: {
    api: string;
    env: string;
    project: string | null;
    proxy: string | null;
    proxyLegacy: boolean;
};

declare module "config" {
    export default config;
}

declare type MetricType = "INT" | "FLOAT" | "PERCENT" | "BOOL" |
    "STRING" | "MILLISEC" | "DATA" | "LEVEL" |
    "DISTRIB" | "RATING" | "WORK_DUR" | "NONE";

declare interface Metric {
    key: string;
    type: MetricType;
    name: string;
    descriptionWidth?: string;
    descriptionHeight?: string;
}

interface MeasureList {
    [propName: string]: number;
}

declare interface TreeElement {
    id: string;
    name: string;
    isNode: boolean;

    children: TreeElement[];

    measures: MeasureList;
    parentId: string | null;
}

declare interface Scale {
    key: string;
    label: string;
}

declare interface Profile {
    id: string;
    name: string;
    metricHeight: Metric;
    metricWidth: Metric;
    scale: Scale;
    description: string;
    editable?: boolean;
}

declare interface Layout {
    id: string;
    name: string;
    description: string;
}

declare interface PreviewPicture {
    forLayout: (l: Layout) => boolean;
    forProfile: (p: Profile) => boolean;
    bgPicture: string;
    contents: null|string|ReactElement<any>;
}
