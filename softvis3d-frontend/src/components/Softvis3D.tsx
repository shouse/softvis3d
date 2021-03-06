import * as React from "react";
import {observer} from "mobx-react";
import CityBuilder from "./citybuilder/CityBuilder";
import Status from "./Status";
import {AppStatusStore} from "../stores/AppStatusStore";
import {CityBuilderStore} from "../stores/CityBuilderStore";
import {SceneStore} from "../stores/SceneStore";
import Visualization from "./visualization/Visualization";

interface Softvis3DProps {
    appStatusStore: AppStatusStore;
    sceneStore: SceneStore;
    cityBuilderStore: CityBuilderStore;
}

@observer
export default class Softvis3D extends React.Component<Softvis3DProps, any> {

    public render() {
        return (
            <div>
                <Status/>
                <CityBuilder store={this.props.cityBuilderStore} appStatusStore={this.props.appStatusStore}/>
                <Visualization cityBuilderStore={this.props.cityBuilderStore} sceneStore={this.props.sceneStore}/>
            </div>
        );
    }
}
