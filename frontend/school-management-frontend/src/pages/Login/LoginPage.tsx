import { useState, type FormEvent } from "react";
import { useNavigate } from "react-router-dom";
import { Section, GlassContainer } from "./styles";


function LoginPage() {

    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const navigate = useNavigate();

    const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();

        try {
            const response = await fetch("http://localhost:8080/auth/login", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify({
                    email,
                    password
                })
            });

            if (!response.ok) {
                throw new Error("E-mail ou senha inválidos");
            }

            const data = await response.json();

            localStorage.setItem("accessToken", data.accessToken);

            navigate("/dashboard");

        } catch (error) {

            console.log(error);
        }
    }

    return (
        <Section>
            <GlassContainer>
                <h2>Login</h2>
                <form onSubmit={handleSubmit}>
                    <div className="input-group">
                        <input type="text" id="username" name="username" placeholder=" " required
                            value={email} onChange={(e) => setEmail(e.target.value)}
                        />
                        <label htmlFor="username">E-mail</label>
                    </div>

                    <div className="input-group">
                        <input type="password" id="password" name="password" placeholder=" " required
                            value={password} onChange={(e) => setPassword(e.target.value)} />
                        <label htmlFor="password">Password</label>
                    </div>

                    <div className="remember-forgot">
                        <label><input type="checkbox" />Remember me</label>
                        <a href="a">Forgot Password?</a>
                    </div>

                    <button type="submit" className="login-btn">Login</button>
                </form>
            </GlassContainer>
        </Section>
    );
}

export default LoginPage;