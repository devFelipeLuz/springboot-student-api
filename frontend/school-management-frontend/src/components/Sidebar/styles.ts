import styled from "styled-components";

export const SidebarContainer = styled.aside`
  width: 240px;
  min-width: 240px;

  padding: 24px 16px;

  background: rgba(15, 23, 42, 0.95);

  border-right: 1px solid rgba(255, 255, 255, 0.08);

  display: flex;
  flex-direction: column;
  gap: 16px;
`;

export const SidebarItem = styled.div`
  display: flex;
  align-items: center;
  gap: 12px;

  padding: 14px;
  border-radius: 12px;

  cursor: pointer;
  transition: 0.25s ease;

  &:hover {
    background: rgba(255, 255, 255, 0.08);
  }
`;