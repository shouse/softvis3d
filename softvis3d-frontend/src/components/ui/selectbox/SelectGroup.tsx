import * as React from "react";
import SelectOption from "./SelectOption";

interface SelectGroupProps {
    selectedValue?: any;
    children?: SelectOption[];
    disabled?: boolean;
    label: string;
}

export default class SelectGroup extends React.Component<SelectGroupProps, any> {
    public static defaultProps = {
        className: "",
        disabled: false
    };

    public render() {
        return (
            <optgroup disabled={this.props.disabled} label={this.props.label}>
                {this.renderChildren()}
            </optgroup>
        );
    }

    private renderChildren(): Array<React.Component<any, any>> {
        return React.Children.map<any>(
            (this.props.children as SelectOption[]),
            (child: React.ReactElement<any>) => {
                if (child.type === SelectOption) {
                    return React.cloneElement(child, {
                        checked: child.props.value === this.props.selectedValue,
                        disabled: this.props.disabled || child.props.disabled
                    });
                } else {
                    return child;
                }
            }
        );
    }
}