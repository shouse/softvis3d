# SoftVis3D - Frontend module

# Installation
1. `npm install`

# Development
1. Start SonarQube-Server
   * `vagrant up` will register an instance of SonarQube to `http://localhost:9001`
2. Configure Dev-Environment (`config/dev.ts`)
   * update `proxy` to point to the SonarQube-Server
   * update `project` for development on a specific Project _(use projectKey)_
4. `npm start` will start the dev-server on `http://localhost:8080`

# Production Build
`npm run build` will execute all available tests before building the project, which can be found in `/app`.

# Unit tests
 * `npm test` will run mocha.
 * `npm test:coverage` will also generate a coverage report

# TODOS
 * If ts-node transpilation fails webpack will carry on and the build will still succeed.
 * A second set integration tests on the transpiled code should be implemented (regression tests)
 
# Code style definitions
 
* Annotations always in a single line

    ```
    @observable
    public showLoadingQueue: boolean;
    ```

* React components input props if more than 2 properties as input given
   
   * more than 1 param

        ```
        interface SideBarProps {
            sceneStore: SceneStore;
            selectedElement: TreeElement | null;
        }
        @observer
        export default class SideBar extends React.Component<SideBarProps, any> {
        ```

   * or single param

        ```
        @observer
        export default class OptionsSimple extends React.Component<{ store: CityBuilderStore; }, any> {
        ```

   * if no property, then use undefined as „object“ type for params

        ```
        @observer
        export default class OptionsSimple extends React.Component<undefined, any> {
        ```