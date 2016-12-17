import * as React from "react";
import { observer } from "mobx-react";
import TopBar from "../topbar/TopBar";
import Scene from "../scene/Scene";
import BottomBar from "../bottombar/BottomBar";
import { CityBuilderStore } from "../../stores/CityBuilderStore";
import { SceneStore } from "../../stores/SceneStore";
import SideBar from "../sidebar/SideBar";
import { TreeService } from "../../layout/TreeService";

interface VisualizationProps {
    sceneStore: SceneStore;
    cityBuilderStore: CityBuilderStore;
}

@observer export default class Visualization extends React.Component<VisualizationProps, any> {
    public render() {
        let selectedElement: TreeElement | null = null;

        /**
         * This logic could also be placed in the store. Not sure for now where to put it.
         * But as the react components should be as dumb as possible i think the store is the better
         * place.
         */
        // TODO: Move this logic in sceneStore.
        if (this.props.sceneStore.legacyData !== null && this.props.sceneStore.selectedObjectId != null) {
            selectedElement =
                TreeService.searchTreeNode(this.props.sceneStore.legacyData, this.props.sceneStore.selectedObjectId);
        }

        return (
            <div>
                <TopBar cityBuilderStore={this.props.cityBuilderStore} selectedElement={selectedElement}/>
                <Scene sceneStore={this.props.sceneStore}/>
                <BottomBar cityBuilderStore={this.props.cityBuilderStore}/>
                <SideBar
                    sceneStore={this.props.sceneStore}
                    selectedElement={selectedElement}
                />
            </div>
        );
    }

}
