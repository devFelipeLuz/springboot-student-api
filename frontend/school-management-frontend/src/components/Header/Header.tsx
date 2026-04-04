import { HeaderContainer, Brand, Navigation } from "./styles";

function Header() {
  return (
    <HeaderContainer>
      <Brand>School API</Brand>

      <Navigation>
        <a href="#">Home</a>
        <a href="#">Contact</a>
      </Navigation>
    </HeaderContainer>
  );
}

export default Header;