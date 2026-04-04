type InputProps = React.InputHTMLAttributes<HTMLInputElement>;

function Input (props: InputProps) {
    return (
        <input {...props} />
    );
}

export default Input;