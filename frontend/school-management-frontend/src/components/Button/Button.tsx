type ButtonProps = React.ButtonHTMLAttributes<HTMLButtonElement>;

function Button (props: ButtonProps) {
    return (
        <button {...props}>{props.children}</button>
    );
}

export default Button;