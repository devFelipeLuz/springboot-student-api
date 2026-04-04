import styled from "styled-components";

export const HeaderContainer = styled.header`
  height: 70px;
  padding: 0 24px;

  display: flex;
  align-items: center;
  justify-content: space-between;

  background: rgba(0, 0, 0, 0.2);
  backdrop-filter: blur(10px);

  border-bottom: 1px solid rgba(255, 255, 255, 0.08);
`;

export const Brand = styled.h1`
  font-size: 20px;
  font-weight: 700;
  cursor: default;
`;

export const Navigation = styled.nav`
  display: flex;
  gap: 24px;

  a {
    color: white;
    text-decoration: none;
    transition: 0.2s;

    &:hover {
      opacity: 0.7;
    }
  }
`;